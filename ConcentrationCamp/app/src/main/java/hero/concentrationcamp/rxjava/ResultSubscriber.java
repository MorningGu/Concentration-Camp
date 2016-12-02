package hero.concentrationcamp.rxjava;


import java.lang.ref.WeakReference;

import hero.concentrationcamp.mvp.IBaseView;
import hero.concentrationcamp.mvp.model.entity.JsonResult;
import hero.concentrationcamp.mvp.model.enums.NetCodeNormal;
import hero.concentrationcamp.utils.ToastUtils;
import io.reactivex.subscribers.ResourceSubscriber;

public class ResultSubscriber extends ResourceSubscriber<JsonResult> {
    private WeakReference<IBaseView> mViewReference;
    private boolean isRefresh = true;
    public ResultSubscriber(IBaseView view){
        mViewReference = new WeakReference<IBaseView>(view);
    }
    public ResultSubscriber(IBaseView view, boolean isRefresh){
        this.isRefresh = isRefresh;
        mViewReference = new WeakReference<IBaseView>(view);
    }


    @Override
    public void onNext(JsonResult jsonResult) {
        if(NetCodeNormal.UPDATE_FORCE.getCode() == jsonResult.getReturnCode()){
            //强制更新
            IBaseView view = getView();
            if(view!=null){
                view.showUpdateDialog(true,"下载地址");
            }
        }else if(NetCodeNormal.LOGIN_TIME_OUT.getCode() == jsonResult.getReturnCode()){
            ToastUtils.showToast(jsonResult.getMsg());
        } else if(NetCodeNormal.SUCCESS.getCode() != jsonResult.getReturnCode()){
            ToastUtils.showToast(jsonResult.getMsg());
        }
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
    protected IBaseView getView(){
        if(mViewReference != null){
            return mViewReference.get();
        }
        return null;
    }
}
