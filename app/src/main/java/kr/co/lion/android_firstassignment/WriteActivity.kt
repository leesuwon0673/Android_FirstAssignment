package kr.co.lion.android_firstassignment

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android_firstassignment.databinding.ActivityWriteBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import kotlin.concurrent.thread

class WriteActivity : AppCompatActivity() {

    lateinit var activityWriteBinding: ActivityWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityWriteBinding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(activityWriteBinding.root)

        setToolbar()
        setView()
        setEvent()
    }
    // 툴바 설정
    fun setToolbar(){
        activityWriteBinding.apply {
            toolbarWrite.apply {
                // 타이틀
                title = "메모 작성"
                // Back버튼
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.write_menu)
            }
        }
    }
    // 뷰 설정
    fun setView(){
        activityWriteBinding.apply {
            // 뷰에 포커스를 준다
            textFieldWriteTitle.requestFocus()
            // 키보드를 올려준다
            focusView(textFieldWriteTitle)

        }

    }
    // 버튼 이벤트
    fun setEvent(){
        activityWriteBinding.apply {
            toolbarWrite.apply {
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.write_menu_item_done -> saveData()
                    }
                    true
                }
            }
        }
    }
    // 데이터 저장
    fun saveData(){
        activityWriteBinding.apply {
            val title = textFieldWriteTitle.text.toString()
            val contents = textFieldWriteContents.text.toString()
            val nowDate = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                LocalDate.now()
            }else{
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                dateFormat.format((Calendar.getInstance().time))
            }
            // 입력칸이 비었을 경우 다이얼로그를 띄워준다
            if(title.isEmpty()){
                viewDialog("제목을 입력하세요",textFieldWriteTitle)
                return
            }
            if(contents.isEmpty()){
                viewDialog("내용을 입력하세요",textFieldWriteContents)
                return
            }
            val data = SaveData(title, contents, nowDate.toString())
            // Intent에 저장한다
            val resultIntent = Intent()
            resultIntent.putExtra("data",data)

            setResult(RESULT_OK,resultIntent)
            finish()
        }
    }

    // 다이얼로그 설정
    fun viewDialog(message:String, focusView:TextInputEditText){
        val builder = MaterialAlertDialogBuilder(this@WriteActivity).apply {
            setTitle("입력 오류")
            setMessage(message)
            setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                focusView.requestFocus()
                focusView(focusView)
            }
        }
        builder.show()

    }
    // 포커스를 주고 키보드를 올리게 설정
    fun focusView(textField:TextInputEditText){
        thread {
            SystemClock.sleep(1000)
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(textField,0)
        }
    }
}