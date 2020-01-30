package com.samaraworkgroup.samarawork.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.samaraworkgroup.samarawork.other.RxUtils;
import com.samaraworkgroup.samarawork.provider.ProviderModule;
import com.samaraworkgroup.samarawork.view.MyBonusesView;
import com.samaraworkgroup.samarawork.view.UseBonusesView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

@InjectViewState
public class UseBonusesPresenter extends MvpPresenter<UseBonusesView> {

    private CompositeDisposable compositeDisposable;

    public UseBonusesPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    private void addBackgroundDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void useMyBonuses(String token, String amount, String category, MultipartBody.Part photo) {
        getViewState().showProgress(true);
        addBackgroundDisposable(
                ProviderModule.getUserProvider().useMyBonuses(token, amount, category, photo)
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(
                                success -> {
                                    getViewState().showProgress(false);
                                    getViewState().useBonuses(success);
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
