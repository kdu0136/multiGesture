// Generated by view binder compiler. Do not edit!
package kim.dongun.viewZoom.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import kim.dongun.viewZoom.R;

public final class TextTagBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final CardView mainLayout;

  @NonNull
  public final AppCompatTextView textView;

  private TextTagBinding(@NonNull CardView rootView, @NonNull CardView mainLayout,
      @NonNull AppCompatTextView textView) {
    this.rootView = rootView;
    this.mainLayout = mainLayout;
    this.textView = textView;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static TextTagBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static TextTagBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
      boolean attachToParent) {
    View root = inflater.inflate(R.layout.text_tag, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static TextTagBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      CardView mainLayout = (CardView) rootView;

      id = R.id.textView;
      AppCompatTextView textView = rootView.findViewById(id);
      if (textView == null) {
        break missingId;
      }

      return new TextTagBinding((CardView) rootView, mainLayout, textView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}