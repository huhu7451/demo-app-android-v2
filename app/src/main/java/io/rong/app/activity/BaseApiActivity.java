package io.rong.app.activity;


import me.add1.exception.BaseException;
import me.add1.network.AbstractHttpRequest;
import me.add1.network.ApiCallback;

/**
 * Created by bob on 2015/1/30.
 */
public abstract class BaseApiActivity extends BaseActivity implements ApiCallback {
    public abstract void onCallApiSuccess(AbstractHttpRequest request, Object obj);

    public abstract void onCallApiFailure(AbstractHttpRequest request, BaseException e);


    @Override
    public void onComplete(final AbstractHttpRequest abstractHttpRequest, final Object o) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCallApiSuccess(abstractHttpRequest,o);
            }
        });
    }

    @Override
    public void onFailure(final AbstractHttpRequest abstractHttpRequest, final BaseException e) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCallApiFailure(abstractHttpRequest, e);
            }
        });
    }
}
