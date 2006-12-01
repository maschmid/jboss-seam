package org.jboss.seam.core;

import static org.jboss.seam.ScopeType.APPLICATION;
import static org.jboss.seam.annotations.Install.BUILT_IN;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.Unwrap;
import org.jbpm.taskmgmt.exe.TaskInstance;

/**
 * Support for the task list.
 * 
 * @see TaskInstanceListForType
 * @see PooledTask
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole</a>
 * @author Gavin King
 */
@Name("org.jboss.seam.core.taskInstanceList")
@Scope(APPLICATION)
@Install(precedence=BUILT_IN, dependencies="org.jboss.seam.core.jbpm")
public class TaskInstanceList
{
   
   @Unwrap
   @Transactional
   public List<TaskInstance> getTaskInstanceList()
   {
      return getTaskInstanceList( Actor.instance().getId() );
   }

   private List<TaskInstance> getTaskInstanceList(String actorId)
   {
      if ( actorId == null ) return null;

      List<TaskInstance> list = new ArrayList<TaskInstance>();
      list.addAll( ManagedJbpmContext.instance().getTaskMgmtSession().findTaskInstances(actorId) );
      return list;
   }
   
}
