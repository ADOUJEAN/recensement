package ci.k2jts.recensement.ui.newRecensement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewRecViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewRecViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is new rec fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}