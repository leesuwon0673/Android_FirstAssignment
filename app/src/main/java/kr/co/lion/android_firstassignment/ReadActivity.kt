package kr.co.lion.android_firstassignment

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.lion.android_firstassignment.databinding.ActivityReadBinding
import java.time.LocalDate

class ReadActivity : AppCompatActivity() {

    lateinit var activityReadBinding: ActivityReadBinding

    lateinit var activityModifyLauncher:ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityReadBinding = ActivityReadBinding.inflate(layoutInflater)
        setContentView(activityReadBinding.root)

        setLauncher()
        setToolbar()
        setView()
        setEvent()
    }
    // 런처 설정
    fun setLauncher(){
        val contract1 = ActivityResultContracts.StartActivityForResult()
        activityModifyLauncher = registerForActivityResult(contract1){
            if(it != null){
                when(it.resultCode){
                    RESULT_OK -> {
                        if(it.data != null){
                            val title = it.data!!.getStringExtra("title")
                            val contents = it.data!!.getStringExtra("contents")
                            // 수정하고 돌아왔을 때 textField의 text를 바꿔준다
                            activityReadBinding.textFieldReadTitle.setText(title)
                            activityReadBinding.textFieldReadContents.setText(contents)

                            // 리스트도 수정한다
                            modifyList(title,contents)
                        }
                    }
                }
            }
        }
    }
    // 수정한 데이터를 리스트에도 수정을 해주는 메서드
    fun modifyList(title:String?,contents:String?){
        val saveData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra("data",SaveData::class.java)
        }else{
            intent.getParcelableExtra<SaveData>("data")
        }
        val allData = MainActivity.saveList
        for(a in 0..<allData.size){
            if(allData[a].contents == saveData?.contents){
                allData[a].contents = contents
                allData[a].title = title
            }
        }
    }
    // 툴바 설정
    fun setToolbar(){
        activityReadBinding.apply {
            toolbarRead.apply {
                // 타이틀
                title = "메모 보기"
                // Back버튼
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.read_menu)
            }
        }
    }
    // 뷰 설정
    fun setView(){
        activityReadBinding.apply {
            // Intent로부터 데이터를 객체에 저장한다
            val saveData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                intent.getParcelableExtra("data",SaveData::class.java)
            }else{
                intent.getParcelableExtra<SaveData>("data")
            }

            textFieldReadTitle.setText(saveData?.title)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                textFieldReadDate.setText("${LocalDate.now()}")
            }
            textFieldReadContents.setText(saveData?.contents)

        }
    }
    // 버튼 이벤트
    fun setEvent(){
        activityReadBinding.apply {
            toolbarRead.apply {
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.read_menu_item_modify -> setModify()

                        R.id.read_menu_item_delete -> setDelete()
                    }
                    true
                }
            }
        }
    }
    // Modify Activity 초기화
    fun setModify(){
        val saveData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra("data",SaveData::class.java)
        }else{
            intent.getParcelableExtra<SaveData>("data")
        }
        val modifyIntent = Intent(this@ReadActivity,ModifyActivity::class.java)
        modifyIntent.putExtra("title",saveData?.title)
        modifyIntent.putExtra("contents",saveData?.contents)
        activityModifyLauncher.launch(modifyIntent)
    }
    // 삭제 처리 설정
    fun setDelete(){
        activityReadBinding.apply {
            val saveData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                intent.getParcelableExtra("data",SaveData::class.java)
            }else{
                intent.getParcelableExtra<SaveData>("data")
            }
            // 리스트 전체를 가져온다
            val allData = MainActivity.saveList

            for(a in 0..<allData.size){
                if(allData[a].contents == saveData?.contents){
                    allData.removeAt(a)
                    Log.d("test1","삭제됨")
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }
}