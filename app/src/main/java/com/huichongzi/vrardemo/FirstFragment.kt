package com.huichongzi.vrardemo

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.huichongzi.vrardemo.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}