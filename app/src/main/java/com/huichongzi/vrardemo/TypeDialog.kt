package com.huichongzi.vrardemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.huichongzi.vrardemo.databinding.DialogTypeSelectBinding

class TypeDialog : DialogFragment() {

    private var _binding : DialogTypeSelectBinding? = null
    private var listener : ((Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogTypeSelectBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.typeSelect?.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.type_stereo -> {
                    listener?.invoke(1)
                }
                else -> {
                    listener?.invoke(0)
                }
            }
            dismiss()
        }
    }

    fun setListener(listener : ((Int) -> Unit)){
        this.listener = listener
    }
}