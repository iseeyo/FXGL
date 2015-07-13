/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.almasb.fxgl.physics;

import java.util.Optional;

import javafx.geometry.Point2D;

/**
 * Result of a raycast. Contains optional entity and point
 * which represent first non ignored physics entity
 * and its point of collision in the ray's path
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 * @version 1.0
 *
 */
public final class RaycastResult {

    private Optional<PhysicsEntity> entity;
    private Optional<Point2D> point;

    /*package-private*/ RaycastResult(Optional<PhysicsEntity> entity, Optional<Point2D> point) {
        this.entity = entity;
        this.point = point;
    }

    /**
     *
     * @return the first physics entity that collided with the ray
     *          whose raycastIgnored flag is false
     */
    public Optional<PhysicsEntity> getEntity() {
        return entity;
    }

    /**
     *
     * @return the collision point in world coordinates
     */
    public Optional<Point2D> getPoint() {
        return point;
    }
}