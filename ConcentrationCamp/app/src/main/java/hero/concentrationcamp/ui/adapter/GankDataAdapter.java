package hero.concentrationcamp.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.ui.WebActivity;
import hero.concentrationcamp.utils.TimeUtils;

/**
 * Created by hero on 2016/12/6 0006.
 */

public class GankDataAdapter extends BaseQuickAdapter<Gank> {
    SourceColumn column;
    public GankDataAdapter(SourceColumn column,List data) {
        super(R.layout.item_gank,data);
        this.column = column;
    }

    @Override
    protected void convert(BaseViewHolder helper, final Gank item) {
        String time = item.getPublishedAt();
        helper.setText(R.id.tv_time, TimeUtils.parseTzTime(time))
                .setText(R.id.tv_creator, item.getWho())
                .setText(R.id.tv_desc, item.getDesc());

      if(column.getCode().equals("福利")){
          //福利的图片是在url中的
          Glide.with(mContext).load(item.getUrl()+"?imageView2/0/w/300").placeholder(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.iv_image));
          helper.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  //福利，点击查看大图
              }
          });
      }else{
          List<String> images = item.getImages();
          if(images!=null && images.size()>0){
              //有图就展示网络图
              Glide.with(mContext).load(images.get(0)+"?imageView2/0/w/300").placeholder(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.iv_image));
          }else{
              //展示占位图
              Glide.with(mContext).load(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.iv_image));
          }
          helper.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  //点击跳转webview 浏览网页详情
                  Intent intent = new Intent(mContext, WebActivity.class);
                  intent.putExtra("url",item.getUrl());
                  intent.putExtra("title",item.getDesc());
                  mContext.startActivity(intent);
              }
          });
      }
    }
}
