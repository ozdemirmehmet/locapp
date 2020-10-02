/*
 * Created by Mehmet Ozdemir on 9/2/20 2:22 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/2/20 2:22 PM
 */

package com.likapalab.locapp.models.interfaces;

import java.io.Serializable;

public interface IOnItemSelectListener extends Serializable {

    void onItemSelect(Object item, int position);

}
