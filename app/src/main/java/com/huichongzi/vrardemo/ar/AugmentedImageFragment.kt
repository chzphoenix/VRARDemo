package com.huichongzi.vrardemo.ar

import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.huichongzi.vrardemo.R


class AugmentedImageFragment : ArFragment() {

    val augmentedImageMap = mutableMapOf<AugmentedImage, Node>()
    lateinit var testModelRenderable : ModelRenderable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        //关闭平面检测的功能，这样才可以进行图片扫描
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ModelRenderable.builder().setSource(activity, R.raw.andy).build().thenAccept{
                renderable ->
            testModelRenderable = renderable
        }

        arSceneView.scene.addOnUpdateListener { frameTime ->
            val frame = arSceneView.arFrame
            frame?.apply {
                val updateAugmentedImages = this.getUpdatedTrackables(AugmentedImage::class.java)
                for (augmentedImage in updateAugmentedImages){
                    when(augmentedImage.trackingState){
                        TrackingState.TRACKING -> {
                            if(!augmentedImageMap.containsKey(augmentedImage)){
                                val anchorNode = AnchorNode()
                                anchorNode.anchor = augmentedImage.createAnchor(augmentedImage.centerPose)
                                anchorNode.setParent(arSceneView.scene)

                                val node = Node()
                                node.setParent(anchorNode)
                                node.renderable = testModelRenderable
                            }
                        }
                        TrackingState.STOPPED -> {
                            augmentedImageMap.remove(augmentedImage)
                        }
                    }
                }
            }
        }
    }

    override fun getSessionConfiguration(session: Session?): Config {
        val config =  super.getSessionConfiguration(session)
        context?.assets?.apply {
//            val bitmap = BitmapFactory.decodeStream(this.open("default.jpg"))
//            val augmentedImageDatabase = AugmentedImageDatabase(session)
//            augmentedImageDatabase.addImage("default.jpg", bitmap)

            val augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, this.open("sample_database.imgdb"))
            config.augmentedImageDatabase = augmentedImageDatabase
        }
        return config
    }
}