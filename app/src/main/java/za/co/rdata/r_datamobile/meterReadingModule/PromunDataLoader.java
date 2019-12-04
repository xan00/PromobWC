package za.co.rdata.r_datamobile.meterReadingModule;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import za.co.rdata.r_datamobile.Models.model_pro_mr_route_header;

/**
 * Project: Promun
 * Created by wcrous on 10/02/2016.
 */
public class PromunDataLoader extends AsyncTaskLoader<List<model_pro_mr_route_header>> {

    private List<model_pro_mr_route_header> mData;

    public PromunDataLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData);
        }

        // Begin monitoring the underlying data source.
//        if (mObserver == null) {
//            mObserver = new SampleObserver();
//            // TODO: register the observer
//        }

        if (takeContentChanged() || mData == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    public void onCanceled(List<model_pro_mr_route_header> data) {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    public void deliverResult(List<model_pro_mr_route_header> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<model_pro_mr_route_header> oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    public List<model_pro_mr_route_header> loadInBackground() {
        List<model_pro_mr_route_header> data = new ArrayList<>();
        return data;
    }

    private void releaseResources(List<model_pro_mr_route_header> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }
}
