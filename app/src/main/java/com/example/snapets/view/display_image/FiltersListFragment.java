package com.example.snapets.view.display_image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snapets.R;
import com.example.snapets.dummy.DummyContent.DummyItem;
import com.example.snapets.view.display_image.utils.BitmapUtils;
import com.example.snapets.view.display_image.utils.SpaceItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FiltersListFragment extends Fragment implements FiltersListFragmentListener{

    RecyclerView recyclerView;
    ThumbnailAdapter adapter;
    List<ThumbnailItem> thumbnailItems;

    FiltersListFragmentListener listener;


    public FiltersListFragmentListener getListener() {
        return listener;
    }

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }




    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FiltersListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FiltersListFragment newInstance(int columnCount) {
        FiltersListFragment fragment = new FiltersListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_filters_list, container, false);

        thumbnailItems = new ArrayList<>();
        adapter = new ThumbnailAdapter(thumbnailItems, this, this.getActivity());

        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpaceItemDecoration(space));
        recyclerView.setAdapter(adapter);

        displayThumbnail(null);

        return itemView;

    }

    public void displayThumbnail(final Bitmap bitmap) {
        Runnable r  = new Runnable() {
            @Override
            public void run() {
                Bitmap thumbImg;
                if(bitmap == null)
                    thumbImg = BitmapUtils.getBitmapFromAssets(getActivity(), DisplayActivity.pictureName, 100, 100);
                else
                    thumbImg = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

                if (thumbImg == null)
                    return;

                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();

                //add normal bitmap first
                ThumbnailItem thumbnailitem = new ThumbnailItem();
                thumbnailitem.image = thumbImg;
                thumbnailitem.filterName = "Normal";
                ThumbnailsManager.addThumb(thumbnailitem);

                List<Filter> filters = FilterPack.getFilterPack(getActivity());

                for (Filter filter : filters){
                    ThumbnailItem t1 = new ThumbnailItem();
                    t1.image = thumbImg;
                    t1.filter = filter;
                    t1.filterName = filter.getName();
                    ThumbnailsManager.addThumb(t1);
                }

                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        new Thread(r).start();
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFilterSelected(Filter filter) {
        if(listener != null){
            listener.onFilterSelected(filter);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
