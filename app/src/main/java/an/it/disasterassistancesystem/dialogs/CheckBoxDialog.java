package an.it.disasterassistancesystem.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.views.ACheckBoxGroup;
import an.it.views.ACheckBox;

/**
 * Created by anit on 11/5/16.
 */

public class CheckBoxDialog extends AlertDialog implements ACheckBoxGroup.OnAllCheckListener{
    private String title;
    private ArrayList<String> lstCheck;
    private ACheckBox mACheckBox;
    private ACheckBoxGroup mACheckboxGroup;
    public CheckBoxDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        getWindow().setLayout(300,300);
        super.onCreate(savedInstanceState);
        setContentView(init());

    }

    private View init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_checkbox, null);
        TextView tvTitle=(TextView)v.findViewById(R.id.tv);
        mACheckBox = (ACheckBox)v.findViewById(R.id.acb_all);
        mACheckboxGroup = (ACheckBoxGroup)v.findViewById(R.id.acbgr);
        tvTitle.setText(title);

        mACheckboxGroup.setCheckList(lstCheck);
        mACheckBox.setOnCheckedChangeListener(new ACheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View var1, boolean var2) {
                mACheckboxGroup.setCheckAll(var2);
            }
        });
        mACheckboxGroup.setOnAllCheckListener(this);
        Button btnClose=(Button) v.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBoxDialog.this.dismiss();
            }
        });

        return v;
    }

    public ArrayList<String> getLstCheck() {
        return lstCheck;
    }

    public void setLstCheck(ArrayList<String> lstCheck) {
        this.lstCheck = lstCheck;
    }
    public ArrayList<String> getCheckedList(){
        return mACheckboxGroup.getCheckedList();
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onAllCheck() {
        mACheckBox.setChecked(true);
    }

    @Override
    public void onAllUnCheck() {
        mACheckBox.setChecked(false);
    }
}
