package me.kennydude.wallet;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.roscopeco.ormdroid.ORMDroidApplication;

import me.kennydude.wallet.CardUtils;

/**
 * @author kennydude
 */
public class ActivityCardAdd extends ListActivity {

	public CardArrayAdapter caa;

	public static class CardArrayAdapter extends ArrayAdapter<CardUtils.CardDescription> {

		public CardArrayAdapter(Context cntx){
			super(cntx,0);
		}

		@Override
		public View getView (int position, View convertView, ViewGroup parent){
			if(convertView == null){
				convertView = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
			}

			CardUtils.CardDescription description = getItem(position);
			((TextView)convertView.findViewById(android.R.id.text1)).setText(description.stringId);

			return convertView;
		}

	}


	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);
		getActionBar().setIcon(R.drawable.ic_app);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		caa = new CardArrayAdapter(this);
		setListAdapter(caa);

		caa.addAll(CardUtils.cards);

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
				CardUtils.CardDescription desc = caa.getItem(pos);

				try{
					Intent i = new Intent(ActivityCardAdd.this, desc.cls.newInstance().getEditActivity());
					i.setAction(CardUtils.ACTION_NEW_CARD);
					startActivityForResult(i, 1);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			setResult(RESULT_OK, data);
			finish();
		}
	}
}
