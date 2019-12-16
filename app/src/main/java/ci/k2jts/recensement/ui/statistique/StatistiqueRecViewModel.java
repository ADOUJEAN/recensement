package ci.k2jts.recensement.ui.statistique;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatistiqueRecViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StatistiqueRecViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(" ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}