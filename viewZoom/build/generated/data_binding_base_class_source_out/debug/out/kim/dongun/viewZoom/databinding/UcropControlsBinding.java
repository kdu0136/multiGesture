// Generated by view binder compiler. Do not edit!
package kim.dongun.viewZoom.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import kim.dongun.viewZoom.R;

public final class UcropControlsBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView controlsShadow;

  @NonNull
  public final ImageView imageViewStateAspectRatio;

  @NonNull
  public final ImageView imageViewStateRotate;

  @NonNull
  public final ImageView imageViewStateScale;

  @NonNull
  public final LinearLayout layoutAspectRatio;

  @NonNull
  public final UcropLayoutRotateWheelBinding layoutRotateWheel;

  @NonNull
  public final UcropLayoutScaleWheelBinding layoutScaleWheel;

  @NonNull
  public final LinearLayout stateAspectRatio;

  @NonNull
  public final LinearLayout stateRotate;

  @NonNull
  public final LinearLayout stateScale;

  @NonNull
  public final FrameLayout wrapperControls;

  @NonNull
  public final LinearLayout wrapperStates;

  private UcropControlsBinding(@NonNull RelativeLayout rootView, @NonNull ImageView controlsShadow,
      @NonNull ImageView imageViewStateAspectRatio, @NonNull ImageView imageViewStateRotate,
      @NonNull ImageView imageViewStateScale, @NonNull LinearLayout layoutAspectRatio,
      @NonNull UcropLayoutRotateWheelBinding layoutRotateWheel,
      @NonNull UcropLayoutScaleWheelBinding layoutScaleWheel,
      @NonNull LinearLayout stateAspectRatio, @NonNull LinearLayout stateRotate,
      @NonNull LinearLayout stateScale, @NonNull FrameLayout wrapperControls,
      @NonNull LinearLayout wrapperStates) {
    this.rootView = rootView;
    this.controlsShadow = controlsShadow;
    this.imageViewStateAspectRatio = imageViewStateAspectRatio;
    this.imageViewStateRotate = imageViewStateRotate;
    this.imageViewStateScale = imageViewStateScale;
    this.layoutAspectRatio = layoutAspectRatio;
    this.layoutRotateWheel = layoutRotateWheel;
    this.layoutScaleWheel = layoutScaleWheel;
    this.stateAspectRatio = stateAspectRatio;
    this.stateRotate = stateRotate;
    this.stateScale = stateScale;
    this.wrapperControls = wrapperControls;
    this.wrapperStates = wrapperStates;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static UcropControlsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static UcropControlsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.ucrop_controls, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static UcropControlsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.controls_shadow;
      ImageView controlsShadow = rootView.findViewById(id);
      if (controlsShadow == null) {
        break missingId;
      }

      id = R.id.image_view_state_aspect_ratio;
      ImageView imageViewStateAspectRatio = rootView.findViewById(id);
      if (imageViewStateAspectRatio == null) {
        break missingId;
      }

      id = R.id.image_view_state_rotate;
      ImageView imageViewStateRotate = rootView.findViewById(id);
      if (imageViewStateRotate == null) {
        break missingId;
      }

      id = R.id.image_view_state_scale;
      ImageView imageViewStateScale = rootView.findViewById(id);
      if (imageViewStateScale == null) {
        break missingId;
      }

      id = R.id.layout_aspect_ratio;
      LinearLayout layoutAspectRatio = rootView.findViewById(id);
      if (layoutAspectRatio == null) {
        break missingId;
      }

      id = R.id.layout_rotate_wheel;
      View layoutRotateWheel = rootView.findViewById(id);
      if (layoutRotateWheel == null) {
        break missingId;
      }
      UcropLayoutRotateWheelBinding binding_layoutRotateWheel = UcropLayoutRotateWheelBinding.bind(layoutRotateWheel);

      id = R.id.layout_scale_wheel;
      View layoutScaleWheel = rootView.findViewById(id);
      if (layoutScaleWheel == null) {
        break missingId;
      }
      UcropLayoutScaleWheelBinding binding_layoutScaleWheel = UcropLayoutScaleWheelBinding.bind(layoutScaleWheel);

      id = R.id.state_aspect_ratio;
      LinearLayout stateAspectRatio = rootView.findViewById(id);
      if (stateAspectRatio == null) {
        break missingId;
      }

      id = R.id.state_rotate;
      LinearLayout stateRotate = rootView.findViewById(id);
      if (stateRotate == null) {
        break missingId;
      }

      id = R.id.state_scale;
      LinearLayout stateScale = rootView.findViewById(id);
      if (stateScale == null) {
        break missingId;
      }

      id = R.id.wrapper_controls;
      FrameLayout wrapperControls = rootView.findViewById(id);
      if (wrapperControls == null) {
        break missingId;
      }

      id = R.id.wrapper_states;
      LinearLayout wrapperStates = rootView.findViewById(id);
      if (wrapperStates == null) {
        break missingId;
      }

      return new UcropControlsBinding((RelativeLayout) rootView, controlsShadow,
          imageViewStateAspectRatio, imageViewStateRotate, imageViewStateScale, layoutAspectRatio,
          binding_layoutRotateWheel, binding_layoutScaleWheel, stateAspectRatio, stateRotate,
          stateScale, wrapperControls, wrapperStates);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
