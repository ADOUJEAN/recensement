package ci.k2jts.recensement.ui.listRecensement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListRecViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListRecViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is liste rec fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}