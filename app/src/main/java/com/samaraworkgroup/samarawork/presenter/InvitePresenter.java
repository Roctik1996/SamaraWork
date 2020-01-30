package com.samaraworkgroup.samarawork.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.samaraworkgroup.samarawork.other.RxUtils;
import com.samaraworkgroup.samarawork.provider.ProviderModule;
import com.samaraworkgroup.samarawork.view.InviteView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class InvitePresenter extends MvpPresenter<InviteView> {

    private CompositeDisposable compositeDisposable;

    public InvitePresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    private void addBackgroundDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void newInvite(String token,
                      Integer amount,
                      String type,
                      String phone,
                      String name,
                      String speciality) {
        getViewState().showProgress(true);
        addBackgroundDisposable(
                ProviderModule.getUserProvider().invite(token, amount, type, phone, name, speciality)
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(
                                success -> {
                                    getViewState().showProgress(false);
                                    getViewState().invite(success);
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
