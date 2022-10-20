package com.huichongzi.vrardemo.ar

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.huichongzi.vrardemo.R


class SceneformArFragment : ArFragment() {
    lateinit var testViewRenderable : ViewRenderable
    lateinit var testModelRenderable : ModelRenderable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val textView = TextView(activity)
        textView.text = "在这里"
        textView.setBackgroundColor(Color.WHITE)
        ViewRenderable.builder().setView(activity, textView).build().thenAccept { renderable ->
            testViewRenderable = renderable
        }

//        MaterialFactory.makeOpaqueWithColor(activity, com.google.ar.sceneform.rendering.Color(Color.RED))
//            .thenAccept { material: Material? ->
//                testModelRenderable =
//                    ShapeFactory.makeSphere(0.1f, Vector3(0.0f, 0.15f, 0.0f), material)
//            }

        ModelRenderable.builder().setSource(activity, R.raw.andy).build().thenAccept{
                renderable ->
            testModelRenderable = renderable
        }


        setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arSceneView.scene)

//            val node = Node()
//            node.setParent(anchorNode)
//            //node.renderable = testViewRenderable
//            node.renderable = testModelRenderable

            val transformableNode = TransformableNode(transformationSystem)
            transformableNode.setParent(anchorNode)
            transformableNode.renderable = testModelRenderable
        }

    }
}