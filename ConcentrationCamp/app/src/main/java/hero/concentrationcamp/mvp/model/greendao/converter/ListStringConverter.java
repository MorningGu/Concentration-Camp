package hero.concentrationcamp.mvp.model.greendao.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * 用于greenDao中字符串list和json的转换
 * Created by hero on 2016/12/14 0014.
 */

public class ListStringConverter implements PropertyConverter<List<String>,String> {
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        Gson gson = new Gson();
        return gson.fromJson(databaseValue, new TypeToken<List<String>>(){}.getType());
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        Gson gson = new Gson();
        return gson.toJson(entityProperty);
    }
}
