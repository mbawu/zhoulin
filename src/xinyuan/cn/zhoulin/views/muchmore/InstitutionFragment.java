package xinyuan.cn.zhoulin.views.muchmore;

import xinyuan.cn.zhoulin.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/**
 * 
 */
public class InstitutionFragment extends Fragment implements OnClickListener {
	private View v;
	private View back;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.institution, container, false);
		initView();
		initlistener();
		return v;
	}

	private void initlistener() {
		back.setOnClickListener(this);

	}

	private void initView() {
		back =  v.findViewById(R.id.back);

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getActivity().getSupportFragmentManager().popBackStack();
			break;

		default:
			break;
		}

	}
}
