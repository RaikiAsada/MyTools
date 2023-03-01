# frozen_string_literal: true
load './dom/jenkins_build.rb'
class JenkinsJob
  attr_reader :jobName

  def initialize(rootDir, jobName)
    @rootDir = rootDir
    @jobName = jobName
  end

  def exist?
    return Dir.exist?(getJobPath)
  end

  def getBuilds
    Dir.chdir(getBuildsPath)
    return Dir.glob('*')
              .select {|file| File.directory?(file)}
              .select { | dir| dir =~ /^\d+$/ }
              .map {|buildDir| JenkinsBuild.new(getJobPath, buildDir, File.birthtime(buildDir))}
  end

  private def getJobPath
    return "#{@rootDir}\\#{@jobName}"
  end

  private def getBuildsPath
    return "#{getJobPath}\\builds"
  end
end
