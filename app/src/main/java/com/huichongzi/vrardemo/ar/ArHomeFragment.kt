package com.huichongzi.vrardemo.ar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.huichongzi.vrardemo.R
import com.huichongzi.vrardemo.databinding.FragmentArHomeBinding
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ArHomeFragment : Fragment() {

    private var _binding: FragmentArHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mSession : Session? = null
    private var mUserRequestedInstall = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentArHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAr()
    }

    fun checkAr(){
        val arEnable = ArCoreApk.getInstance().checkAvailability(activity)
        if(arEnable.isTransient){
            Handler().postDelayed({checkAr()}, 200)
        }
        else if(arEnable.isSupported){
            when(arEnable){
                ArCoreApk.Availability.SUPPORTED_INSTALLED -> {
                    mSession = Session(activity)
                }
                ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD,  ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED -> {
                    installARCore()
                }
            }
        }
        else{
            Log.d("arcore", "不支持arcore")
        }
    }

    override fun onResume() {
        super.onResume()
        installARCore()
    }

    fun installARCore(){
        activity?.let{
            if(ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA), 0)
                return
            }

            try {
                if (mSession == null) {
                    when(ArCoreApk.getInstance().requestInstall(it, mUserRequestedInstall)){
                        ArCoreApk.InstallStatus.INSTALLED -> {
                            mSession = Session(it)
                            init()
                        }
                        ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                            // When this method returns `INSTALL_REQUESTED`:
                            // 1. ARCore pauses this activity.
                            // 2. ARCore prompts the user to install or update Google Play
                            //    Services for AR (market://details?id=com.google.ar.core).
                            // 3. ARCore downloads the latest device profile data.
                            // 4. ARCore resumes this activity. The next invocation of
                            //    requestInstall() will either return `INSTALLED` or throw an
                            //    exception if the installation or update did not succeed.
                            mUserRequestedInstall = false
                        }
                    }
                }
                else{
                    init()
                }
            }
            catch (e: UnavailableUserDeclinedInstallationException) {
                //用户先前拒绝安装（有可能是无法安装），无法使用ARCore
                Log.e("arcore", "install fial", e)
            }
            catch (e : Exception){
                Log.e("arcore", "install fial", e)
            }
        }
    }

    fun init(){
        _binding?.arcore?.setOnClickListener {
            findNavController().navigate(R.id.action_arHomeFragment_to_ARCoreFragment, null)
        }
        _binding?.myArFragment?.setOnClickListener {
            findNavController().navigate(R.id.action_arHomeFragment_to_myArFragment, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mSession?.close()
    }
}