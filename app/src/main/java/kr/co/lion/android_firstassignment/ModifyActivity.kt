package kr.co.lion.android_firstassignment

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android_firstassignment.databinding.ActivityModifyBinding
import kotlin.concurrent.thread

class ModifyActivity : AppCompatActivity() {

    lateinit var activityModifyBinding: ActivityModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityModifyBinding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(activityModifyBinding.root)

        setToolbar()
        setView()
        setEvent()
    }
    // 툴바 설정
    fun setToolbar(){
        activityModifyBinding.apply {
            toolbarModify.apply {
                // 타이틀
                title = "메모 수정"
                // Back버튼
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.modify_menu)
            }
        }
    }
    // 뷰 설정
    fun setView(){
        activityModifyBinding.apply {
            val title = intent.getStringExtra("title")
            val contents = intent.getStringExtra("contents")
            textFieldModifyTitle.setText("${title}")
            textFieldModifyContents.setText("${contents}")
        }
    }
    // 버튼 이벤트
    fun setEvent(){
        activityModifyBinding.apply {
            toolbarModify.apply {
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.modify_menu_item_done -> processModify()
                    }
                    true
                }
            }
        }
    }
    // 수정 완료 과정 처리
    fun processModify(){
        activityModifyBinding.apply {
            val title = textFieldModifyTitle.text.toString()
            val contents = textFieldModifyContents.text.toString()

            if(title.isEmpty()){
                viewDialog("제목을 입력하세요",textFieldModifyTitle)
                return
            }
            if(contents.isEmpty()){
                viewDialog("내용을 입력하세요",textFieldModifyContents)
                return
            }
            // intent 객체 생성
            val resultIntent = Intent()
            resultIntent.putExtra("title",title)
            resultIntent.putExtra("contents",contents)
            setResult(RESULT_OK,resultIntent)
            finish()
        }
    }
    // 다이얼로그 설정
    fun viewDialog(message:String, focusView: TextInputEditText){
        val builder = MaterialAlertDialogBuilder(this@ModifyActivity).apply {
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
    fun focusView(textField: TextInputEditText){
        thread {
            SystemClock.sleep(1000)
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(textField,0)
        }
    }
}