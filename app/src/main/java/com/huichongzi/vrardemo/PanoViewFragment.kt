package com.huichongzi.vrardemo

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.huichongzi.vrardemo.databinding.FragmentPanoViewBinding

class PanoViewFragment : Fragment() {
    private var _binding: FragmentPanoViewBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPanoViewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.apply {
            val option = VrPanoramaView.Options()

            val uri = arguments?.getParcelable<Uri>("uri")
            if(uri != null){
                var bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                option.inputType = arguments?.getInt("type") ?: VrPanoramaView.Options.TYPE_MONO
                _binding?.vrPano?.loadImageFromBitmap(bitmap, option)
            }
            else if(arguments?.getBoolean("isMono") == true){
                var bitmap = BitmapFactory.decodeStream(assets.open("monoimage.jpeg"))
                option.inputType = VrPanoramaView.Options.TYPE_MONO
                _binding?.vrPano?.loadImageFromBitmap(bitmap, option)
            }
            else {
                var bitmap = BitmapFactory.decodeStream(assets.open("andes.jpeg"))
                option.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER
                _binding?.vrPano?.loadImageFromBitmap(bitmap, option)
            }

            _binding?.vrPano?.setEventListener(object : VrPanoramaEventListener() {
                override fun onLoadSuccess() {
                    super.onLoadSuccess()
                }

                override fun onLoadError(errorMessage: String?) {
                    super.onLoadError(errorMessage)
                }

                override fun onClick() {
                    super.onClick()
                }

                override fun onDisplayModeChanged(newDisplayMode: Int) {
                    super.onDisplayModeChanged(newDisplayMode)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        _binding?.vrPano?.resumeRendering()
    }

    override fun onPause() {
        super.onPause()
        _binding?.vrPano?.pauseRendering()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _binding?.vrPano?.shutdown()
    }
}