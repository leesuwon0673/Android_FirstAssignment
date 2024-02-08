package kr.co.lion.android_firstassignment

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.android_firstassignment.databinding.ActivityMainBinding
import kr.co.lion.android_firstassignment.databinding.RowMainBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // WriteActivity 런처
    lateinit var activityWriteLauncher:ActivityResultLauncher<Intent>

    // ModifyActivity 런처
    lateinit var activityReadLauncher: ActivityResultLauncher<Intent>

    // 메모 제목과 내용 작성날짜를 담아둘 리스트
    companion object{
        var saveList = mutableListOf<SaveData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setLauncher()
        setToolbar()
        setRecyclerView()
        setEvent()
    }
    // 런처 설정
    fun setLauncher(){
        val contract1 = ActivityResultContracts.StartActivityForResult()
        activityWriteLauncher = registerForActivityResult(contract1){
            // resultCode가 RESULT_OK인지 확인
            if(it.resultCode == RESULT_OK){
                // 데이터가 비었는지 확인
                if(it.data != null){
                    // 빌드 버전에 맞게 나눈다
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        val saveData = it.data?.getParcelableExtra("data",SaveData::class.java)
                        saveList.add(saveData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    }else{
                        val saveData = it.data?.getParcelableExtra<SaveData>("data")
                        saveList.add(saveData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
        val contract2 = ActivityResultContracts.StartActivityForResult()
        activityReadLauncher = registerForActivityResult(contract2){
            activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
        }
    }
    // 툴바 설정
    fun setToolbar(){
        activityMainBinding.apply {
            toolbarMain.apply {
                // 타이틀
                title = "메모 관리"
                // 메뉴
                inflateMenu(R.menu.main_menu)
            }
        }
    }
    // 버튼 이벤트
    fun setEvent(){
        activityMainBinding.apply {
            toolbarMain.apply {
                // 메뉴의 버튼을 눌렀을 때..
                // 메뉴 아이템 id에 따라 분기해준다
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.main_menu_item_add ->{
                            val writeIntent = Intent(this@MainActivity,WriteActivity::class.java)
                            activityWriteLauncher.launch(writeIntent)
                        }
                    }
                    true
                }
            }
        }
    }
    // 리사이클러 뷰 설정
    fun setRecyclerView(){
        activityMainBinding.apply {
            recyclerViewMain.apply {
                adapter = RecyclerViewAdapter()

                layoutManager = LinearLayoutManager(this@MainActivity)

                val deco = MaterialDividerItemDecoration(this@MainActivity,MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // recyclerView 어뎁터 설정
    inner class RecyclerViewAdapter:RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        // ViewHolder 클래스
        inner class ViewHolder(rowMainBinding: RowMainBinding):RecyclerView.ViewHolder(rowMainBinding.root){
            val rowMainBinding:RowMainBinding

            init{
                this.rowMainBinding = rowMainBinding

                this.rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                this.rowMainBinding.root.setOnClickListener {
                    val modifyIntent = Intent(this@MainActivity,ReadActivity::class.java)
                    modifyIntent.putExtra("data",saveList[adapterPosition])
                    activityReadLauncher.launch(modifyIntent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(rowMainBinding)

            return viewHolder
        }

        override fun getItemCount(): Int {
            return saveList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.rowMainBinding.textViewRow1.text = "${saveList[position].title}"
            holder.rowMainBinding.textViewRow2.text = "${saveList[position].nowDate}"
        }
    }
}