/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tez.dag.api;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public abstract class TezEntityDescriptor implements Writable {

  protected byte[] userPayload;
  private String className;

  @Private // for Writable
  public TezEntityDescriptor() {
  }
  
  public TezEntityDescriptor(String className) {
    this.className = className;
  }

  public byte[] getUserPayload() {
    return this.userPayload;
  }

  public TezEntityDescriptor setUserPayload(byte[] userPayload) {
    this.userPayload = userPayload;
    return this;
  }

  public String getClassName() {
    return this.className;
  }
  
  @Override
  public void write(DataOutput out) throws IOException {
    Text.writeString(out, className);
    if (userPayload == null) {
      out.writeInt(-1);
    } else {
      out.writeInt(userPayload.length);
      out.write(userPayload);
    }
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    this.className = Text.readString(in);
    int payloadLength = in.readInt();
    if (payloadLength != -1) {
      userPayload = new byte[payloadLength];
      in.readFully(userPayload);
    }
  }
}
