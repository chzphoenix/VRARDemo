package com.huichongzi.vrardemo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.vr.sdk.widgets.video.VrVideoView
import com.huichongzi.vrardemo.databinding.FragmentFirstBinding
import com.huichongzi.vrardemo.vr.TypeDialog

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val getVideo = registerForActivityResult(ActivityResultContracts.GetContent()){ uri : Uri? ->
        uri?.let { uri ->
            var bundle = Bundle()
            bundle.putParcelable("uri", uri)
            findNavController().navigate(R.id.action_FirstFragment_to_zoomVideo, bundle)
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

        _binding?.vrdemo?.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_vrFragment, null)
        }

        _binding?.zoomvideo?.setOnClickListener {
            //getVideo.launch("video/*")
            getVideo.launch("*/*")   //浏览所有文件
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}