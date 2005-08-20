//$Id$
package org.jboss.seam.example.booking;

import static javax.persistence.GeneratorType.AUTO;
import static org.jboss.seam.ScopeType.EVENT;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.Length;
import org.hibernate.validator.Pattern;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("hotel")
@Scope(EVENT)
public class Hotel implements Serializable
{
   private Long id;
   private String address;
   private String city;
   private String zip;
   
   @Id(generate=AUTO)
   public Long getId()
   {
      return id;
   }
   public void setId(Long id)
   {
      this.id = id;
   }
   
   @Length(max=100)
   public String getAddress()
   {
      return address;
   }
   public void setAddress(String address)
   {
      this.address = address;
   }
   
   @Length(max=20)
   public String getCity()
   {
      return city;
   }
   public void setCity(String city)
   {
      this.city = city;
   }
   
   @Length(min=4, max=5)
   @Pattern(regex="\\d{4,5}")
   public String getZip()
   {
      return zip;
   }
   public void setZip(String zip)
   {
      this.zip = zip;
   }
   
   public String toString()
   {
      return "Hotel(" + address + "," + city + "," + zip + ")";
   }
   
}
