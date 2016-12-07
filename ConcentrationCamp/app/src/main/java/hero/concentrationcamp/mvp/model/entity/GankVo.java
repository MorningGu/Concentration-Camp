package hero.concentrationcamp.mvp.model.entity;

import java.util.List;

/**
 * Created by hero on 2016/12/6 0006.
 */

public class GankVo {
    private boolean error;
    private List<Gank> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Gank> getResults() {
        return results;
    }

    public void setResults(List<Gank> results) {
        this.results = results;
    }
}
