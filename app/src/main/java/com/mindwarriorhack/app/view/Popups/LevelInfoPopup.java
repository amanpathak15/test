package com.mindwarriorhack.app.view.Popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.Levels.LevelsListItem;

import java.util.List;

public class LevelInfoPopup extends AlertDialog {

    private TextView level_title, level_description;
    private ImageView cancel;
    private String levelName,description,title;
    private List<LevelsListItem> levelsList;

    public LevelInfoPopup(Context context,String description,String title) {
        super(context);
        this.description=description;
        this.title=title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_info_popup);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initUI();
    }

    public void initUI(){
        level_title=findViewById(R.id.level_title);
        level_title.setText(title);
        level_description =findViewById(R.id.level_info);
        //level_description.setMovementMethod(ScrollingMovementMethod.getInstance());
        level_description.setText(description);
        cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
