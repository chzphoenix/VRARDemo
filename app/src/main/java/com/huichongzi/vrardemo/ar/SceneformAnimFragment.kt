package com.huichongzi.vrardemo.ar

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.SkeletonNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.huichongzi.vrardemo.R


class SceneformAnimFragment : ArFragment() {
    lateinit var bodyRenderable : ModelRenderable
    lateinit var modelAnimator : ModelAnimator
    lateinit var hatRenderable : ModelRenderable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ModelRenderable.builder().setSource(activity, R.raw.andy_dance).build().thenAccept{
                renderable ->
            bodyRenderable = renderable

            val animData = bodyRenderable.getAnimationData("andy_dance")
            modelAnimator = ModelAnimator(animData, bodyRenderable)
        }

        ModelRenderable.builder().setSource(activity, R.raw.baseball_cap).build().thenAccept{
                renderable ->
            hatRenderable = renderable
        }


        setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arSceneView.scene)

            val bodyNode = SkeletonNode()
            bodyNode.setParent(anchorNode)
            bodyNode.renderable = bodyRenderable

            val boneNode = Node()
            boneNode.setParent(bodyNode)
            bodyNode.setBoneAttachment("hat_point", boneNode)

            val hatNode = Node()
            hatNode.renderable = hatRenderable
            hatNode.setParent(boneNode)
            hatNode.worldScale = Vector3.one()
            hatNode.worldRotation = Quaternion.identity()

            val pos = hatNode.worldPosition
            // Lower the hat down over the antennae.
            pos.y -= .1f
            hatNode.worldPosition = pos

            modelAnimator.start()
        }

    }
}