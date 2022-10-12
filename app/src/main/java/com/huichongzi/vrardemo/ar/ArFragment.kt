package com.huichongzi.vrardemo.ar

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.huichongzi.vrardemo.databinding.FragmentArBinding
import com.huichongzi.vrardemo.vr.TypeDialog

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ArFragment : Fragment() {

    private var _binding: FragmentArBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentArBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        _binding?.vrdemo?.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_vrFragment, null)
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}