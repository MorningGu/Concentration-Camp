package hero.concentrationcamp.mvp.data;

import java.util.ArrayList;
import java.util.List;

import hero.concentrationcamp.mvp.model.entity.Source;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;

/**
 * Created by hero on 2016/11/30 0030.
 */

public class MainActivityDataFactory {
    private List<Source> mSources;
    public MainActivityDataFactory(){

    }

    private Source initGank(){
        Source gank = new Source();
        gank.setName("干货集中营");
        gank.setCode(0);
        List<SourceColumn> gankColumns = new ArrayList<>();
        gankColumns.add(new SourceColumn("Android","Android"));
        gankColumns.add(new SourceColumn("iOS","iOS"));
        gankColumns.add(new SourceColumn("休息视频","休息视频"));
        gankColumns.add(new SourceColumn("福利","福利"));
        gankColumns.add(new SourceColumn("拓展资源","拓展资源"));
        gankColumns.add(new SourceColumn("前端","前端"));
        gankColumns.add(new SourceColumn("瞎推荐","瞎推荐"));
        gankColumns.add(new SourceColumn("App","App"));
        gank.setColumns(gankColumns);
        return gank;
    }
    private Source initDuanZi(){
        Source duanzi = new Source();
        duanzi.setName("段子");
        duanzi.setCode(1);
        List<SourceColumn>duanziColumns = new ArrayList<>();
        duanziColumns.add(new SourceColumn("段子","QueryJokeByTime"));
        duanziColumns.add(new SourceColumn("趣图","QueryImgByTime"));
        return duanzi;
    }
    private Source initMeiPai(){
        Source meipai = new Source();
        meipai.setName("美拍");
        meipai.setCode(2);
        return meipai;
    }
}
