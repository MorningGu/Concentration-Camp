package hero.concentrationcamp.mvp;

/**
 * 所有BaseView实体类都要实现的接口，是基础类要实现的，不是给子类实现的，也不是给子View的接口继承的
 * Created by hero on 2016/11/30 0030.
 * z
 */

public interface IBaseView {
    /**
     * 需要放到View最开始初始化的方法中，用来初始化presenter
     */
    void initPresenter();

    /**
     * 用来弹出更新的dialog
     * @param isForce 是否为强制更新
     * @param url 下载地址
     */
    void showUpdateDialog(boolean isForce, String url);
}
