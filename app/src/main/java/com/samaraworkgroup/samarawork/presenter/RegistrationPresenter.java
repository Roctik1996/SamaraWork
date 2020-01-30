package com.samaraworkgroup.samarawork.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.samaraworkgroup.samarawork.other.RxUtils;
import com.samaraworkgroup.samarawork.provider.ProviderModule;
import com.samaraworkgroup.samarawork.view.RegistrationView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

@InjectViewState
public class RegistrationPresenter extends MvpPresenter<RegistrationView> {
    private CompositeDisposable compositeDisposable;

    public RegistrationPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    private void addBackgroundDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void getSignUp(String key,
                          String username,
                          String password,
                          String city,
                          String phone,
                          Integer team,
                          Boolean trip,
                          String specialist,
                          MultipartBody.Part photo,
                          String token) {
        getViewState().showProgress(true);

        addBackgroundDisposable(
                ProviderModule.getUserProvider().signup(key, username, password, city, phone, team, trip, specialist, photo, token)
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(
                                success -> {
                                    getViewState().registration(success);
                                    getViewState().showProgress(false);
                                },
                                error -> {
                                    getViewState().showError(error.getMessage());
                                    getViewState().showProgress(false);
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
