package com.samaraworkgroup.samarawork.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.samaraworkgroup.samarawork.other.RxUtils;
import com.samaraworkgroup.samarawork.provider.ProviderModule;
import com.samaraworkgroup.samarawork.view.WorkView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

@InjectViewState
public class WorkPresenter extends MvpPresenter<WorkView> {
    private CompositeDisposable compositeDisposable;

    public WorkPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    private void addBackgroundDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void newWork(String token,
                        Integer amount,
                        String type,
                        String phone,
                        String objectCity,
                        String objectName,
                        String summ,
                        MultipartBody.Part photo) {
        getViewState().showProgress(true);
        addBackgroundDisposable(
                ProviderModule.getUserProvider().work(token, amount, type,phone, objectCity, objectName, summ, photo)
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(
                                success -> {
                                    getViewState().showProgress(false);
                                    getViewState().work(success);
                                },
                                error -> {
                                    getViewState().showProgress(false);
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
