//$Id$
package org.jboss.seam.example.booking;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Interceptor;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.logging.Logger;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.ejb.SeamInterceptor;

@Stateful
@Name("findHotels")
@LocalBinding(jndiBinding="findHotels")
@Interceptor(SeamInterceptor.class)
@LoggedIn
public class FindHotelsAction implements FindHotels, Serializable
{
   private static final Logger log = Logger.getLogger(FindHotels.class);
   
   @PersistenceContext
   private EntityManager em;
   
   private String searchString;
   private List<Hotel> hotels;
   
   @Out(required=false)
   private Hotel hotel;
   
   @In(required=false) 
   @Out(required=false)
   private Booking booking;
   
   @In
   private User user;
   
   @Out
   private DataModel hotelsDataModel = new ListDataModel();
   int rowIndex = 0;
   
   @Begin
   public String find()
   {
      hotel = null;
      hotels = em.createQuery("from Hotel where lower(city) like :search or lower(zip) like :search or lower(address) like :search")
            .setParameter("search", '%' + searchString.toLowerCase().replace('*', '%') + '%')
            .setMaxResults(50)
            .getResultList();
      
      log.info(hotels.size() + " hotels found");
      
      hotelsDataModel.setWrappedData(hotels);
      
      return "main";
   }
   
   public String getSearchString()
   {
      return searchString;
   }

   public void setSearchString(String searchString)
   {
      this.searchString = searchString==null ? 
            "*" : searchString;
   }
   
   public String selectHotel()
   {
      if (hotels==null) return "main";
      rowIndex = hotelsDataModel.getRowIndex();
      setHotel();
      return "selected";
   }

   public String nextHotel()
   {
      if (hotels==null) return "main";
      if ( rowIndex<hotels.size()-1 )
      {
         hotelsDataModel.setRowIndex(++rowIndex);
         setHotel();
      }
      return "redisplay";
   }

   public String lastHotel()
   {
      if (hotels==null) return "main";
      if (rowIndex>0)
      {
         hotelsDataModel.setRowIndex(--rowIndex);
         setHotel();
      }
      return "redisplay";
   }

   private void setHotel()
   {
      hotel = (Hotel) hotelsDataModel.getRowData();
      log.info( rowIndex + "=>" + hotel );
   }
   
   public String bookHotel()
   {
      if (hotel==null) return "main";
      booking = new Booking(hotel, user);
      booking.setCheckinDate( new Date() );
      booking.setCheckoutDate( new Date() );
      return "book";
   }
   
   @End @Remove
   public String confirm()
   {
      if (booking==null || hotel==null) return "main";
      em.persist(booking);
      log.info("booking confirmed");
      return "confirmed";
   }
      
   @Destroy @Remove
   public void destroy() {}
}
