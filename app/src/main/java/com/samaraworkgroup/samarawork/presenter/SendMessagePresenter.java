package com.samaraworkgroup.samarawork.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.samaraworkgroup.samarawork.other.RxUtils;
import com.samaraworkgroup.samarawork.provider.ProviderModule;
import com.samaraworkgroup.samarawork.view.AllMessageView;
import com.samaraworkgroup.samarawork.view.SendMessageView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

@InjectViewState
public class SendMessagePresenter extends MvpPresenter<SendMessageView> {

    private CompositeDisposable compositeDisposable;

    public SendMessagePresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    private void addBackgroundDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void sendMessage(String token, String text, Integer id, MultipartBody.Part photo) {
        addBackgroundDisposable(
                ProviderModule.getUserProvider().sendMessage(token, text, id, photo)
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(
                                success -> {
                                    getViewState().send(success);
                                },
                                error -> {
                                    getViewState().showError(error.getMessage());
                                }
                        ));
    }

    @Override
    protected void finalize() throws Throwable {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        super.finalize();
    }
}
