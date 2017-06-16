/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxgl.scene

import com.almasb.fxgl.ecs.Entity
import com.almasb.fxgl.entity.component.BoundingBoxComponent
import com.almasb.fxgl.entity.component.PositionComponent
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D

/**
 * Game scene viewport.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
class Viewport

/**
 * Constructs a viewport with given width and height.
 *
 * @param width viewport width
 * @param height viewport height
 */
(
        /**
         * @return viewport width
         */
        val width: Double,

        /**
         * @return viewport height
         */
        val height: Double) {

    /**
     * @return current visible viewport area
     */
    val visibleArea: Rectangle2D
        get() = Rectangle2D(getX(), getY(), getX() + width, getY() + height)

    /**
     * Origin x.
     */
    private val x = SimpleDoubleProperty()
    fun getX() = x.get()
    fun xProperty() = x
    fun setX(x: Double) = xProperty().set(x)

    /**
     * Origin y.
     */
    private val y = SimpleDoubleProperty()
    fun getY() = y.get()
    fun yProperty() = y
    fun setY(y: Double) = yProperty().set(y)

    private val zoom = SimpleDoubleProperty(1.0)
    fun getZoom() = zoom.get()
    fun zoomProperty() = zoom
    fun setZoom(value: Double) = zoomProperty().set(value)

    /**
     * @return viewport origin (x, y)
     */
    val origin: Point2D
        get() = Point2D(getX(), getY())

    /**
     * Binds the viewport to entity so that it follows the given entity.
     * distX and distY represent bound distance between entity and viewport origin.
     *
     * bindToEntity(player, getWidth() / 2, getHeight() / 2);
     *
     * the code above centers the camera on player.
     *
     * @param entity the entity to follow
     *
     * @param distX distance in X between origin and entity
     *
     * @param distY distance in Y between origin and entity
     */
    fun bindToEntity(entity: Entity, distX: Double, distY: Double) {
        val position = entity.getComponentOptional(PositionComponent::class.java)
                .orElseThrow{ IllegalArgumentException("Cannot bind to entity without PositionComponent") }

        // origin X Y with no bounds
        val bx = position.xProperty().add(-distX)
        val by = position.yProperty().add(-distY)

        // origin X Y with bounds applied
        var boundX = Bindings.`when`(bx.lessThan(minX)).then(minX).otherwise(position.xProperty().add(-distX))
        var boundY = Bindings.`when`(by.lessThan(minY)).then(minY).otherwise(position.yProperty().add(-distY))

        boundX = Bindings.`when`(bx.greaterThan(maxX.subtract(width))).then(maxX.subtract(width)).otherwise(boundX)
        boundY = Bindings.`when`(by.greaterThan(maxY.subtract(height))).then(maxY.subtract(height)).otherwise(boundY)

        x.bind(boundX)
        y.bind(boundY)
    }

    fun bindToFit(xMargin: Double, yMargin: Double, vararg entities: Entity) {
        val minBindingX = entities.filter { it.hasComponent(BoundingBoxComponent::class.java) }
                .map { it.getComponent(BoundingBoxComponent::class.java) }
                .map { it.minXWorldProperty() }
                .fold(Bindings.min(SimpleIntegerProperty(Int.MAX_VALUE), Integer.MAX_VALUE), { min, x -> Bindings.min(min, x) })
                .subtract(xMargin)

        val minBindingY = entities.filter { it.hasComponent(BoundingBoxComponent::class.java) }
                .map { it.getComponent(BoundingBoxComponent::class.java) }
                .map { it.minYWorldProperty() }
                .fold(Bindings.min(SimpleIntegerProperty(Int.MAX_VALUE), Integer.MAX_VALUE), { min, y -> Bindings.min(min, y) })
                .subtract(yMargin)

        val maxBindingX = entities.filter { it.hasComponent(BoundingBoxComponent::class.java) }
                .map { it.getComponent(BoundingBoxComponent::class.java) }
                .map { it.maxXWorldProperty() }
                .fold(Bindings.max(SimpleIntegerProperty(Int.MIN_VALUE), Integer.MIN_VALUE), { max, x -> Bindings.max(max, x) })
                .add(xMargin)

        val maxBindingY = entities.filter { it.hasComponent(BoundingBoxComponent::class.java) }
                .map { it.getComponent(BoundingBoxComponent::class.java) }
                .map { it.maxYWorldProperty() }
                .fold(Bindings.max(SimpleIntegerProperty(Int.MIN_VALUE), Integer.MIN_VALUE), { max, y -> Bindings.max(max, y) })
                .add(yMargin)

        val widthBinding = maxBindingX.subtract(minBindingX)
        val heightBinding = maxBindingY.subtract(minBindingY)

        val ratio = Bindings.min(Bindings.divide(width, widthBinding), Bindings.divide(height, heightBinding))

        x.bind(minBindingX)
        y.bind(minBindingY)

        zoom.bind(ratio)
    }

    /**
     * Unbind viewport.
     */
    fun unbind() {
        xProperty().unbind()
        yProperty().unbind()
        zoomProperty().unbind()
    }

    private val minX = SimpleIntegerProperty(Integer.MIN_VALUE)
    private val minY = SimpleIntegerProperty(Integer.MIN_VALUE)
    private val maxX = SimpleIntegerProperty(Integer.MAX_VALUE)
    private val maxY = SimpleIntegerProperty(Integer.MAX_VALUE)

    /**
     * Set bounds to viewport so that the viewport will not move outside the bounds
     * when following an entity.
     *
     * @param minX min x
     * @param minY min y
     * @param maxX max x
     * @param maxY max y
     */
    fun setBounds(minX: Int, minY: Int, maxX: Int, maxY: Int) {
        this.minX.set(minX)
        this.minY.set(minY)
        this.maxX.set(maxX)
        this.maxY.set(maxY)
    }
}