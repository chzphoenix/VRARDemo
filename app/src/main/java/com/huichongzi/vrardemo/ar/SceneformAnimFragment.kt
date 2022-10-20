package com.huichongzi.vrardemo.ar

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.huichongzi.vrardemo.R


class SceneformAnimFragment : ArFragment() {
    lateinit var bodyRenderable : ModelRenderable
    lateinit var modelAnimator : ModelAnimator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ModelRenderable.builder().setSource(activity, R.raw.andy_dance).build().thenAccept{
                renderable ->
            bodyRenderable = renderable

            val animData = bodyRenderable.getAnimationData("andy_dance")
            modelAnimator = ModelAnimator(animData, bodyRenderable)
        }


        setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arSceneView.scene)

            val transformableNode = TransformableNode(transformationSystem)
            transformableNode.setParent(anchorNode)
            transformableNode.renderable = bodyRenderable

            modelAnimator.start()
        }

    }
}