package hero.concentrationcamp.ui.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.model.entity.Joke;

/**
 * Created by hero on 2016/12/8 0008.
 */

public class JokeDataAdapter extends BaseQuickAdapter<Joke> {


    public JokeDataAdapter(List<Joke> data) {
        super(R.layout.item_joke,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Joke item) {
        helper.setText(R.id.tv_time, item.getCt())
              .addOnClickListener(R.id.btn_collect)
              .addOnClickListener(R.id.btn_share);
        if(item.getType()==1){
            //文字的时候显示text
            helper.setText(R.id.tv_desc, Html.fromHtml(item.getText()));
        }else{
            //图片的时候显示title
            helper.setText(R.id.tv_desc,item.getTitle());
        }
        if(!TextUtils.isEmpty(item.getImg())){
            helper.getView(R.id.iv_image).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.iv_image);
            Glide.with(mContext).load(item.getImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((ImageView) helper.getView(R.id.iv_image));
        }else{
            helper.getView(R.id.iv_image).setVisibility(View.GONE);
        }

    }
}
