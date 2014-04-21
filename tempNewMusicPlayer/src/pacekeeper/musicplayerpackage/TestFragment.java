package pacekeeper.musicplayerpackage;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TestFragment extends Fragment {


	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Toast.makeText(getActivity(), "HI3", Toast.LENGTH_SHORT).show();
		
		View view=(LinearLayout)inflater.inflate(R.layout.frag_artlists_song_layout, container, false);
		ImageButton ib=(ImageButton) view.findViewById(R.id.bt_bt);
		
		return view;
	}
	
	
}
