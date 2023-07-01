package com.lickling.mymusic.ui.home.PQ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.NetEaseUser;
import com.lickling.mymusic.databinding.FragmentDesktopFourBinding;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.ui.load.LoadActivity;
import com.lickling.mymusic.ui.local.LocalActivity;
import com.lickling.mymusic.ui.login.LoginNetEase;
import com.lickling.mymusic.ui.setting.home.SettingHomeActivity;
import com.lickling.mymusic.viewmodel.UserViewModel;
import com.orm.SugarContext;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Desktop_four#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentDesktopFourBinding desktopFourBinding;
    private UserViewModel userViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    public UserFragment(UserViewModel model) {
        // Required empty public constructor
        this.userViewModel = model;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Desktop_four.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        desktopFourBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_desktop_four, container, false);
        desktopFourBinding.setUserInfo(userViewModel);
        return desktopFourBinding.getRoot();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: netEase user name: " + userViewModel.getNetEaseName());
        userViewModel.upgradeNteEaseInfo(desktopFourBinding.headshot);


        desktopFourBinding.setUserInfo(userViewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        desktopFourBinding.setUserInfo(userViewModel);
        userViewModel.setNetEaseAvatar(desktopFourBinding.headshot);
    }

    public void setUserInfo(UserViewModel userInfo) {
        this.userViewModel = userInfo;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        desktopFourBinding.headshot.setOnClickListener(view -> {
            if (userViewModel.isLoginNetEase())
                Toast.makeText(getActivity(), "已登录网易", Toast.LENGTH_SHORT).show();
            else startActivity(new Intent(getActivity(), LoginNetEase.class));
        });


        desktopFourBinding.headshot.setOnLongClickListener(view -> {

            if (userViewModel.logoutNetEase())
                Toast.makeText(getActivity(), "网易：退出成功", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getActivity(), "网易：退出失败", Toast.LENGTH_SHORT).show();
            return false;
        });
        desktopFourBinding.imageviewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                v.startAnimation(animation);
                Intent intent = new Intent(getActivity(), SettingHomeActivity.class);
                startActivity(intent);
            }
        });

        desktopFourBinding.local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), LocalActivity.class));
            }
        });
        desktopFourBinding.downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoadActivity.class));
            }
        });

        desktopFourBinding.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Vip_pay.class));
            }
        });
    }
}