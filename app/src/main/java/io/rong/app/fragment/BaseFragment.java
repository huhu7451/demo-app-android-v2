package io.rong.app.fragment;

import android.support.v4.app.Fragment;

import me.add1.exception.BaseException;
import me.add1.network.AbstractHttpRequest;
import me.add1.network.ApiCallback;

/**
 * Created by Bob on 2015/03/12.
 */
public abstract class BaseFragment extends Fragment implements ApiCallback {
    public abstract void onCallApiSuccess(AbstractHttpRequest request, Object obj);

    public abstract void onCallApiFailure(AbstractHttpRequest request, BaseException e);


    @Override
    public void onComplete(final AbstractHttpRequest abstractHttpRequest, final Object o) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCallApiSuccess(abstractHttpRequest,o);
            }
        });
    }

    @Override
    public void onFailure(final AbstractHttpRequest abstractHttpRequest, final BaseException e) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCallApiFailure(abstractHttpRequest, e);
            }
        });
    }
}
