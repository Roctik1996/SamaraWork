package com.samaraworkgroup.samarawork.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.samaraworkgroup.samarawork.other.RxUtils;
import com.samaraworkgroup.samarawork.provider.ProviderModule;
import com.samaraworkgroup.samarawork.view.AllChatView;
import com.samaraworkgroup.samarawork.view.GetAllQuestion;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class GetQuestionPresenter extends MvpPresenter<GetAllQuestion> {

    private CompositeDisposable compositeDisposable;

    public GetQuestionPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    private void addBackgroundDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void getQuestion(String token) {
        getViewState().showProgress(true);
        addBackgroundDisposable(
                ProviderModule.getUserProvider().getAllQuestion(token)
                        .compose(RxUtils.ioToMainTransformerSingle())
                        .subscribe(
                                success -> {
                                    getViewState().showProgress(false);
                                    getViewState().getAll(success);
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
