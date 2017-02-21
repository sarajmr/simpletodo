package com.sarajmudigonda.mysimpletodo;

/**
 * Created by saraj.mudigonda on 1/28/2017.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Point;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.button;
import static android.R.attr.data;
import static android.R.attr.dialogLayout;
import static com.raizlabs.android.dbflow.config.FlowLog.Level.D;
import static com.sarajmudigonda.mysimpletodo.R.id.simpleDatePicker;
// ...

public class EditNameDialogFragment extends DialogFragment implements TextView.OnEditorActionListener{

    private EditText mEditText;
    private TextView mEditTextView, mEditPrice;
    private ImageView mEditImage;
    int itemNum;

    public EditNameDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }


    public static EditNameDialogFragment newInstance(String title) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        /*
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        */

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_edit, container);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        mEditTextView = (TextView) view.findViewById(R.id.tvFoodDesc);
        mEditImage = (ImageView) view.findViewById(R.id.ivDialogImage);
        mEditPrice = (TextView)view.findViewById(R.id.tvPrice);

       // Fetch arguments from bundle and set title
        String title = getArguments().getString("DialogTitle");
        //mEditText.setText(getArguments().getString("itemValue"));
        mEditText.setVisibility(view.GONE);
        itemNum = getArguments().getInt("itemPos");
        // Removing the Title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        //getDialog().setTitle(title);
        //Log.d("myTag", title);


        if (itemNum == 0) {
            mEditImage.setImageResource(R.drawable.asian_tofu_salad);
            mEditTextView.setText("Asian Tofu Salad");
            mEditPrice.setText("$10.99");
        }
        else if (itemNum == 1) {
            mEditImage.setImageResource(R.drawable.chicken_tostada_salad);
            mEditTextView.setText("Chicken Tostada Salad");
            mEditPrice.setText("$11.49");
        }
        else if (itemNum == 2) {
            mEditImage.setImageResource(R.drawable.greek_salad_with_chicken);
            mEditTextView.setText("Greek Salad With Chicken");
            mEditPrice.setText("$12.49");
        }
        else if (itemNum == 3) {
                mEditImage.setImageResource(R.drawable.grilled_tofu);
                mEditTextView.setText("Grilled Tofu");
                mEditPrice.setText("$9.99");
            }
        else {
                mEditImage.setImageResource(R.drawable.paneer_tikka_masala);
                mEditTextView.setText("Paneer Tikka Masala");
                mEditPrice.setText("$12.99");
        }

        final Button btnCancel = (Button) view.findViewById(R.id.btnCancel);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                dismiss();
            }
        });

        final DatePicker dateOrder = (DatePicker)view.findViewById(R.id.simpleDatePicker);

        final Button btnOrder = (Button) view.findViewById(R.id.btnSave);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                int dayOrder = dateOrder.getDayOfMonth();
                int monthOrder = dateOrder.getMonth() + 1;
                int yearOrder = dateOrder.getYear();
                String placeOrder = monthOrder + "/"  + dayOrder + "/" + yearOrder;
                //Toast.makeText(getContext(), "DatePicker " + placeOrder, Toast.LENGTH_SHORT).show();
                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                listener.onFinishEditDialog(placeOrder, itemNum);
                dismiss();
            }
        });



       // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // 2. Setup a callback when the "Done" button is pressed on keyboard
        mEditText.setOnEditorActionListener(this);
    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText, int inputNum );
    }

    // Fires whenever the textfield has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString(), itemNum);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 50% of the screen width and 25% of height
        //window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setLayout((int) (size.x *.90), (int) (size.y *.90));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}


