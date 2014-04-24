package pacekeeper.musicplayerpackage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ListFragment_SortByPlayList extends ListFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = (LinearLayout) inflater.inflate(
				R.layout.lf_sortbyplaylist_layout, container, false);
		return view;
	}
}
