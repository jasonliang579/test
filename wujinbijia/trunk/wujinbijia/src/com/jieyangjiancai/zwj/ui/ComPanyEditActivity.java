package com.jieyangjiancai.zwj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.ToastMessage;

public class ComPanyEditActivity extends BaseActivity{
    private EditText company_ed;
    public final static String EXTRA_EDS = "extra_eds";
    public final static String EXTRA_DES = "extra_des";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_companyedit);
        
        initView();
    }

    private void initView() {
        TextView title = (TextView)findViewById(R.id.title_bar_text);
        title.setText("公司介绍");
        
        company_ed = (EditText)findViewById(R.id.company_ed);
        company_ed.setText(getIntent().getStringExtra(EXTRA_EDS));
        
        findViewById(R.id.company_bt_ok).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                if(company_ed.getText().toString() != null && !company_ed.getText().toString().trim().equals("")){
                    Intent data = new Intent();
                    data.putExtra(EXTRA_EDS, company_ed.getText().toString());
                    setResult(RESULT_OK, data);
                    finish();
                }else{
                    ToastMessage.show(ComPanyEditActivity.this, "请输入公司介绍");
                }
            }
        });
    }
}
