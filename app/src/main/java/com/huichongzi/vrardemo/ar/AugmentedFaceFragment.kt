package com.huichongzi.vrardemo.ar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.*
import com.google.ar.core.Config.AugmentedFaceMode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.AugmentedFaceNode
import com.huichongzi.vrardemo.R
import java.util.*
import java.util.function.Consumer


class AugmentedFaceFragment : ArFragment() {

    private lateinit var faceRegionsRenderable: ModelRenderable
    private lateinit var faceMeshTexture: Texture
    private val faceNodeMap = HashMap<AugmentedFace, AugmentedFaceNode>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        //关闭平面检测的功能，这样才可以进行图片扫描
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Load the face regions renderable.
        // This is a skinned model that renders 3D objects mapped to the regions of the augmented face.
        ModelRenderable.builder()
            .setSource(activity, R.raw.fox_face)
            .build()
            .thenAccept(
                Consumer { modelRenderable: ModelRenderable ->
                    faceRegionsRenderable = modelRenderable
                    modelRenderable.isShadowCaster = false
                    modelRenderable.isShadowReceiver = false
                })

        // Load the face mesh texture.

        // Load the face mesh texture.
        Texture.builder()
            .setSource(activity, R.drawable.fox_face_mesh_texture)
            .build()
            .thenAccept(Consumer { texture: Texture ->
                faceMeshTexture = texture
            })

        arSceneView.scene.addOnUpdateListener { frameTime ->
            val faceList: Collection<AugmentedFace> =
                arSceneView.session?.getAllTrackables<AugmentedFace>(
                    AugmentedFace::class.java
                ) ?: listOf()

            //为每个面部添加素材
            for (face in faceList) {
                if (!faceNodeMap.containsKey(face)) {
                    val faceNode = AugmentedFaceNode(face)
                    faceNode.setParent(arSceneView.scene)
                    faceNode.faceRegionsRenderable = faceRegionsRenderable
                    faceNode.faceMeshTexture = faceMeshTexture
                    faceNodeMap.put(face, faceNode)
                }
            }

            //当检测状态是STOPPED，则移除素材
            val iter: MutableIterator<Map.Entry<AugmentedFace, AugmentedFaceNode>> =
                faceNodeMap.entries.iterator()
            while (iter.hasNext()) {
                val (face, faceNode) = iter.next()
                if (face.trackingState == TrackingState.STOPPED) {
                    faceNode.setParent(null)
                    iter.remove()
                }
            }
        }
    }

    override fun getSessionConfiguration(session: Session?): Config? {
        val config = Config(session)
        config.augmentedFaceMode = AugmentedFaceMode.MESH3D
        return config
    }

    override fun getSessionFeatures(): Set<Session.Feature?>? {
        return EnumSet.of(Session.Feature.FRONT_CAMERA)
    }
}