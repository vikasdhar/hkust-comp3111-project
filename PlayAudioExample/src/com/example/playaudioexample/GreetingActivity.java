package com.example.playaudioexample;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GreetingActivity extends Activity {
	TextView greetMsg;
	ImageButton showalbumartButton2;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.greeting);
		
		greetMsg = (TextView) findViewById(R.id.textView1);
		Intent intename = getIntent();
		String uname = (String) intename.getSerializableExtra("USERNAME");
		greetMsg.setText("Welcome " + uname);
		showalbumartButton2 = (ImageButton)findViewById(R.id.showalbumart2);
		showalbumartButton2.setOnClickListener(new onButtonClick2());
	}
	
	public class onButtonClick2 implements View.OnClickListener {
		public void onClick(View view) {
//			Intent intent=new Intent(GreetingActivity.this,
//					PlayAudioExample.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 
//			startActivity(intent);
		}
	}
}
//// album art part; to resize after knowing the actual image height
//ivAlbumArt = (ImageView) findViewById(R.id.pedo_vp_collapse_albumart);
//ViewTreeObserver vto = ivAlbumArt.getViewTreeObserver();
//vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//    public boolean onPreDraw() {
//        finalHeight = ivAlbumArt.getMeasuredHeight();
//        ivAlbumArt.getLayoutParams().width = finalHeight;
//        return true;
//    }
//});
//
//Toast.makeText(this, "The duration is" + player.getDuration(),
//		Toast.LENGTH_SHORT).show();

//Toast.makeText(this, "Now playing: " + currentFile, Toast.LENGTH_SHORT)
//.show();
