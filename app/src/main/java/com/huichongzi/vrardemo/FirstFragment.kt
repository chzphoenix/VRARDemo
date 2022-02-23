package com.huichongzi.vrardemo

import android.app.Dialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.google.vr.sdk.widgets.video.VrVideoView
import com.huichongzi.vrardemo.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val getPano = registerForActivityResult(ActivityResultContracts.GetContent()){ uri : Uri? ->
        uri?.let { uri ->
            activity?.apply {
                val dialog = TypeDialog()
                dialog.setListener { type ->
                    var bundle = Bundle()
                    bundle.putInt("type", if(type == 0) VrPanoramaView.Options.TYPE_MONO else VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER)
                    bundle.putParcelable("uri", uri)
                    findNavController().navigate(R.id.action_FirstFragment_to_panoViewFragment, bundle)
                }
                dialog.show(supportFragmentManager, null)
            }
        }
    }

    val getVideo = registerForActivityResult(ActivityResultContracts.GetContent()){ uri : Uri? ->
        uri?.let { uri ->
            activity?.apply {
                val dialog = TypeDialog()
                dialog.setListener { type ->
                    var bundle = Bundle()
                    bundle.putInt("type", if(type == 0) VrVideoView.Options.TYPE_MONO else VrVideoView.Options.TYPE_STEREO_OVER_UNDER)
                    bundle.putParcelable("uri", uri)
                    findNavController().navigate(R.id.action_FirstFragment_to_vrVideoFragment, bundle)
                }
                dialog.show(supportFragmentManager, null)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.panoStereo?.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean("isMono", false)
            findNavController().navigate(R.id.action_FirstFragment_to_panoViewFragment, bundle)
        }
        _binding?.panoMono?.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean("isMono", true)
            findNavController().navigate(R.id.action_FirstFragment_to_panoViewFragment, bundle)
        }
        _binding?.vrVideoStereo?.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean("isMono", false)
            findNavController().navigate(R.id.action_FirstFragment_to_vrVideoFragment, bundle)
        }
        _binding?.vrVideoMono?.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean("isMono", true)
            findNavController().navigate(R.id.action_FirstFragment_to_vrVideoFragment, bundle)
        }
        _binding?.panoLocal?.setOnClickListener {
            getPano.launch("image/*")
        }
        _binding?.videoLocal?.setOnClickListener {
            getVideo.launch("video/*")
        }
        _binding?.gvrDemo?.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_gvrDemoActivity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}