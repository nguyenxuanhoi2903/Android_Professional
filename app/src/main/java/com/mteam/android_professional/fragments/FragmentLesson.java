package com.mteam.android_professional.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mteam.android_professional.Contants;
import com.mteam.android_professional.R;
import com.mteam.android_professional.Utils;
import com.mteam.android_professional.activity.DetailActivity;
import com.mteam.android_professional.adapter.AdapterViewPagerLesson;
import com.mteam.android_professional.customview.ShadowTransformer;
import com.mteam.android_professional.interfaces.ICardView;
import com.mteam.android_professional.obj.Chapter;
import com.mteam.android_professional.obj.Lesson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import me.huseyinozer.TooltipIndicator;

/**
 * Created by Stealer Of Souls on 2/17/2018.
 */

public class FragmentLesson extends Fragment implements ICardView {
    private ViewPager viewPager;
    private AdapterViewPagerLesson adapterViewPagerLesson;
    private int idChapter;
    private ShadowTransformer mCardShadowTransformer;
    private TooltipIndicator tooltipIndicator;
    private List<Lesson> lessonList;
    private ImageView img;
    private TextView txtMotivation;
    private Chapter chapter;
    private ImageView mImgBack;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_lesson, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        findViewByIds();
        initComponent();


    }
    @SuppressLint("NewApi")
    private void initData() {
        chapter = (Chapter) getArguments().getSerializable(Contants.CHAPTER);
        if (chapter != null) {
            idChapter = chapter.getIdChapter();
            String jsonString = Utils.readFileFromAssets("data/lesson_android_c" + idChapter + ".txt", getContext());
            if (!jsonString.isEmpty() && jsonString != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Lesson>>() {
                }.getType();
                lessonList = gson.fromJson(jsonString, type);
            }

        }




    }

    private void findViewByIds() {
        viewPager = getView().findViewById(R.id.viewPager_lesson);
        tooltipIndicator = getView().findViewById(R.id.tooltip_indicator);
        img = getView().findViewById(R.id.img_);
        txtMotivation = getView().findViewById(R.id.txt_motivation);
        mImgBack=getView().findViewById(R.id.img_arrow_left);
    }

    public void setImageFromAssets(ImageView mImage, Context context, String nameImage) {
        try {
            // get input stream
            InputStream ims = context.getAssets().open(nameImage);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            mImage.setImageDrawable(d);
            ims.close();
        } catch (IOException ex) {
            return;
        }
    }

    @SuppressLint("NewApi")
    private void initComponent() {
        // img.setImageBitmap(BitmapFactory.decodeByteArray(chapter.getImgChapter(), 0 ,chapter.getImgChapter().length));
        setImageFromAssets(img, img.getContext(), "images/"+chapter.getImgChapter());
        txtMotivation.setText(chapter.getMotivation());

        adapterViewPagerLesson = new AdapterViewPagerLesson(this);
        if (lessonList != null && lessonList.size() > 0) {
            for (Lesson x : lessonList) {
                adapterViewPagerLesson.addCardItem(x);
            }

        }

        mCardShadowTransformer = new ShadowTransformer(viewPager, adapterViewPagerLesson);
        viewPager.setAdapter(adapterViewPagerLesson);
        viewPager.setPageTransformer(false, mCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);
        // tooltipIndicator.setupViewPager(viewPager);
        // mCardShadowTransformer.enableScaling(true);
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }
    @SuppressLint("NewApi")
    @Override
    public void itemClick(int position) {
        Lesson lesson = lessonList.get(position);
        if (!lesson.isKey()) {
            Toast.makeText(getContext(), "Bài học sẽ được cập nhập trong phiên bản tới", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Contants.LESSON, lesson);
        intent.putExtra(Contants.ID_CHAPTER,idChapter);
        getActivity().startActivity(intent);

    }


}