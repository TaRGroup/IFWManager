package trumeet.IfwManager;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Trumeet on 2017/3/27.
 * @author Trumeet
 * @since 1.0
 */

public class App extends Application {
    @Override
    public void onCreate () {
        super.onCreate();
        Utils.init(this);
    }
}
